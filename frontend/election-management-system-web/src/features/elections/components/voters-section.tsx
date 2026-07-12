"use client";

import { useState, useRef } from "react";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { EmptyState } from "./empty-state";
import { SectionCard } from "./section-card";
import { ViewAllDialog } from "./view-all-dialog";
import { useDownloadBulkVoterSchema } from "../hooks/use-download-bulk-voter-schema";
import { useBulkUploadVoters } from "../hooks/use-bulk-upload-voters";
import { useLocalStorage } from "@/hooks/use-local-storage";
import { usePagination } from "@/hooks/use-pagination";
import { UserDetails } from "@/features/auth/types/user-details";
import { STORAGE_KEYS } from "@/constants/storage-keys";
import { ElectionVoter } from "../types";
import {
  MoreHorizontal,
  Plus,
  Trash2,
  Upload,
  FileDown,
  CheckCircle2,
  Eye,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

type Props = {
  voters: ElectionVoter[];
  electionId: string;
};

export function VotersSection({ voters, electionId }: Props) {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [step, setStep] = useState<"schema" | "upload">("schema");
  const [csvFile, setCsvFile] = useState<File | null>(null);
  const [hasExistingTemplate, setHasExistingTemplate] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [userData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );

  const itemsPerPage = 5;
  const {
    page,
    setPage,
    totalPages,
    paginatedItems: paginatedVoters,
  } = usePagination(voters, itemsPerPage);
  const [viewAllOpen, setViewAllOpen] = useState(false);
  const [viewAllSearch, setViewAllSearch] = useState("");

  const filteredVoters = viewAllSearch
    ? voters.filter(
        (v) =>
          v.fullName.toLowerCase().includes(viewAllSearch.toLowerCase()) ||
          v.email.toLowerCase().includes(viewAllSearch.toLowerCase())
      )
    : voters;

  const downloadSchema = useDownloadBulkVoterSchema({
    userId: userData?.userId,
    electionId,
  });

  const bulkUpload = useBulkUploadVoters({
    userId: userData?.userId,
    electionId,
    onSuccess: () => {
      setDialogOpen(false);
      setStep("schema");
      setCsvFile(null);
    },
  });

  const openDialog = () => {
    setStep("schema");
    setCsvFile(null);
    setHasExistingTemplate(false);
    setDialogOpen(true);
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] ?? null;
    setCsvFile(file);
  };

  const handleUpload = () => {
    if (!csvFile) return;
    bulkUpload.mutate(csvFile);
  };

  const handleDownloadTemplate = () => {
    downloadSchema.mutate(undefined, {
      onSuccess: () => setStep("upload"),
    });
  };

  const bulkUploadButton = (
    <Button variant="outline" onClick={openDialog}>
      <Upload className="mr-2 h-4 w-4" />
      Bulk Upload
    </Button>
  );

  const voterTable = voters.length > 0 && (
    <>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Name</TableHead>
            <TableHead>Email</TableHead>
            <TableHead className="w-30 text-right">Actions</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {paginatedVoters.map((voter, index) => (
            <TableRow key={voter.id ?? index}>
              <TableCell className="font-medium">{voter.fullName}</TableCell>

              <TableCell>{voter.email}</TableCell>

              <TableCell className="text-right">
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" size="icon">
                      <MoreHorizontal className="h-4 w-4" />
                    </Button>
                  </DropdownMenuTrigger>

                  <DropdownMenuContent align="end">
                    <DropdownMenuItem className="text-destructive focus:text-destructive">
                      <Trash2 className="mr-2 h-4 w-4" />
                      Remove Voter
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      {voters.length > itemsPerPage && (
        <div className="flex items-center justify-between pt-4">
          <p className="text-muted-foreground text-sm">
            Showing {page * itemsPerPage + 1}-
            {Math.min((page + 1) * itemsPerPage, voters.length)} of{" "}
            {voters.length}
          </p>
          <div className="flex items-center gap-1">
            <Button
              variant="ghost"
              size="sm"
              disabled={page === 0}
              onClick={() => setPage(page - 1)}
            >
              <ChevronLeft className="h-4 w-4" />
            </Button>
            {Array.from({ length: totalPages }, (_, i) => (
              <Button
                key={i}
                variant={page === i ? "primary" : "ghost"}
                size="sm"
                className="min-w-8"
                onClick={() => setPage(i)}
              >
                {i + 1}
              </Button>
            ))}
            <Button
              variant="ghost"
              size="sm"
              disabled={page >= totalPages - 1}
              onClick={() => setPage(page + 1)}
            >
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        </div>
      )}
    </>
  );

  return (
    <>
      <SectionCard
        title="Election Voters"
        actions={
          voters.length === 0 ? (
            bulkUploadButton
          ) : (
            <div className="flex gap-2">
              {voters.length > itemsPerPage && (
                <Button variant="outline" onClick={() => setViewAllOpen(true)}>
                  <Eye className="mr-2 h-4 w-4" />
                  View All ({voters.length})
                </Button>
              )}
              <Button>
                <Plus className="mr-2 h-4 w-4" />
                Add Voter
              </Button>
            </div>
          )
        }
      >
        {voters.length === 0 ? (
          <EmptyState
            title="No voters found"
            description="Upload voters in bulk to get started."
            action={bulkUploadButton}
          />
        ) : (
          voterTable
        )}
      </SectionCard>

      <ViewAllDialog
        open={viewAllOpen}
        onOpenChange={(open) => {
          setViewAllOpen(open);
          if (!open) setViewAllSearch("");
        }}
        title="All Voters"
        totalCount={filteredVoters.length}
        searchValue={viewAllSearch}
        onSearchChange={setViewAllSearch}
        searchPlaceholder="Search by name or email..."
      >
        {(displayCount) => (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead className="w-30 text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredVoters.slice(0, displayCount).map((voter, index) => (
                <TableRow key={voter.id ?? index}>
                  <TableCell className="font-medium">
                    {voter.fullName}
                  </TableCell>
                  <TableCell>{voter.email}</TableCell>
                  <TableCell className="text-right">
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon">
                          <MoreHorizontal className="h-4 w-4" />
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem className="text-destructive focus:text-destructive">
                          <Trash2 className="mr-2 h-4 w-4" />
                          Remove Voter
                        </DropdownMenuItem>
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </ViewAllDialog>

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>Bulk Upload Voters</DialogTitle>
            {step === "schema" ? (
              <DialogDescription>
                Download the CSV template, fill it in with voter details, and
                upload it back. If you already have a template, you can upload
                it directly.
              </DialogDescription>
            ) : (
              <DialogDescription>
                {hasExistingTemplate
                  ? "Upload your CSV file to add voters."
                  : "Template downloaded. Upload the filled CSV file to add voters."}
              </DialogDescription>
            )}
          </DialogHeader>

          {step === "schema" ? (
            <>
              <div className="flex flex-col gap-3">
                <Button
                  variant="outline"
                  className="h-auto justify-start gap-3 py-4"
                  onClick={handleDownloadTemplate}
                  disabled={downloadSchema.isPending}
                >
                  <FileDown className="h-5 w-5 shrink-0" />
                  <div className="text-left">
                    <p className="font-medium">Download template</p>
                    <p className="text-muted-foreground text-xs">
                      Get a blank CSV template with the required columns
                    </p>
                  </div>
                </Button>

                <Button
                  variant="outline"
                  className="h-auto justify-start gap-3 py-4"
                  onClick={() => {
                    setStep("upload");
                    setHasExistingTemplate(true);
                  }}
                >
                  <Upload className="h-5 w-5 shrink-0" />
                  <div className="text-left">
                    <p className="font-medium">Already have template</p>
                    <p className="text-muted-foreground text-xs">
                      Upload your existing CSV file with voter data
                    </p>
                  </div>
                </Button>
              </div>

              <DialogFooter>
                <Button variant="outline" onClick={() => setDialogOpen(false)}>
                  Cancel
                </Button>
              </DialogFooter>
            </>
          ) : (
            <>
              {hasExistingTemplate ? (
                <div className="text-muted-foreground flex items-center gap-2 rounded-lg border p-3 text-sm">
                  <Upload className="h-4 w-4 shrink-0" />
                  Upload your CSV file with voter data.
                </div>
              ) : (
                <div className="flex items-center gap-2 rounded-lg border border-green-200 bg-green-50 p-3 text-sm text-green-700 dark:border-green-800 dark:bg-green-950 dark:text-green-400">
                  <CheckCircle2 className="h-4 w-4 shrink-0" />
                  Template downloaded successfully. Fill in the CSV and upload
                  it below.
                </div>
              )}

              <div className="space-y-2">
                <label className="text-sm font-medium">Upload CSV File</label>

                <Input
                  ref={fileInputRef}
                  type="file"
                  accept=".csv"
                  onChange={handleFileChange}
                />

                {csvFile && (
                  <p className="text-muted-foreground text-xs">
                    Selected: {csvFile.name}
                  </p>
                )}
              </div>

              <DialogFooter>
                <Button
                  variant="outline"
                  onClick={() => {
                    setStep("schema");
                    setCsvFile(null);
                    setHasExistingTemplate(false);
                  }}
                >
                  Back
                </Button>
                <Button
                  onClick={handleUpload}
                  disabled={!csvFile || bulkUpload.isPending}
                >
                  {bulkUpload.isPending ? (
                    "Uploading..."
                  ) : (
                    <>
                      <Upload className="mr-2 h-4 w-4" />
                      Upload Voters
                    </>
                  )}
                </Button>
              </DialogFooter>
            </>
          )}
        </DialogContent>
      </Dialog>
    </>
  );
}
