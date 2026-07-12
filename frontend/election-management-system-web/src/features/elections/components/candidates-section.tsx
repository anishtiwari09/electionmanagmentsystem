"use client";

import { useState, useRef } from "react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
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
import { useDownloadBulkSchema } from "../hooks/use-download-bulk-schema";
import { useBulkUploadCandidates } from "../hooks/use-bulk-upload-candidates";
import { useLocalStorage } from "@/hooks/use-local-storage";
import { usePagination } from "@/hooks/use-pagination";
import { UserDetails } from "@/features/auth/types/user-details";
import { STORAGE_KEYS } from "@/constants/storage-keys";
import { ElectionCandidate, ElectionPositionWithCandidate } from "../types";
import {
  MoreHorizontal,
  Upload,
  Plus,
  Trash2,
  ArrowRightLeft,
  FileDown,
  CheckCircle2,
  Eye,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

type Props = {
  candidates: ElectionCandidate[];
  positions: ElectionPositionWithCandidate[];
  electionId: string;
};

export function CandidatesSection({
  candidates,
  positions,
  electionId,
}: Props) {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [counts, setCounts] = useState<Record<string, number>>({});
  const [step, setStep] = useState<"schema" | "upload">("schema");
  const [csvFile, setCsvFile] = useState<File | null>(null);
  const [hasExistingTemplate, setHasExistingTemplate] = useState(false);
  const [validationErrors, setValidationErrors] = useState<string[] | null>(
    null
  );
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [userData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );

  const [viewAllOpen, setViewAllOpen] = useState(false);
  const [viewAllSearch, setViewAllSearch] = useState("");
  const itemsPerPage = 5;

  const filteredCandidates = viewAllSearch
    ? candidates.filter(
        (c) =>
          c.fullName.toLowerCase().includes(viewAllSearch.toLowerCase()) ||
          c.email.toLowerCase().includes(viewAllSearch.toLowerCase()) ||
          (
            positions
              .find((p) => p.electionPositionId === c.positionId)
              ?.positionName.toLowerCase() || ""
          ).includes(viewAllSearch.toLowerCase())
      )
    : candidates;
  const {
    page,
    setPage,
    totalPages,
    paginatedItems: paginatedCandidates,
  } = usePagination(candidates, itemsPerPage);

  const downloadSchema = useDownloadBulkSchema({
    userId: userData?.userId,
    electionId,
  });

  const bulkUpload = useBulkUploadCandidates({
    userId: userData?.userId,
    electionId,
    onSuccess: () => {
      setDialogOpen(false);
      setStep("schema");
      setCsvFile(null);
    },
  });

  const openDialog = () => {
    const initial: Record<string, number> = {};
    positions.forEach((p) => {
      initial[p.electionPositionId] = Math.max(
        p.maxSelection,
        p.candidates?.length ?? 0
      );
    });
    setCounts(initial);
    setStep("schema");
    setCsvFile(null);
    setHasExistingTemplate(false);
    setValidationErrors(null);
    setDialogOpen(true);
  };

  const handleCountChange = (id: string, value: string) => {
    const num = parseInt(value, 10);
    if (!isNaN(num) && num >= 0) {
      setCounts((prev) => ({ ...prev, [id]: num }));
    }
  };

  const hasError = positions.some(
    (p) =>
      !counts[p.electionPositionId] ||
      counts[p.electionPositionId] < p.maxSelection
  );

  const handleCreateSchema = () => {
    const payload = {
      positions: positions.map((p) => ({
        electionPositionId: p.electionPositionId,
        numberOfCandidates: counts[p.electionPositionId] ?? 1,
      })),
      electionId: electionId,
    };
    downloadSchema.mutate(payload, {
      onSuccess: () => setStep("upload"),
    });
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] ?? null;
    setCsvFile(file);
  };

  const handleUpload = () => {
    if (!csvFile) return;
    bulkUpload.mutate(csvFile);
  };

  const content = (
    <>
      {positions.length === 0 ? null : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Name</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Position</TableHead>
              <TableHead className="w-30 text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {paginatedCandidates.map((candidate) => (
              <TableRow key={candidate.id}>
                <TableCell className="font-medium">
                  {candidate.fullName}
                </TableCell>

                <TableCell>{candidate.email}</TableCell>

                <TableCell>
                  <Badge variant="secondary">
                    {positions.find(
                      (p) => p.electionPositionId === candidate.positionId
                    )?.positionName ?? "-"}
                  </Badge>
                </TableCell>

                <TableCell className="text-right">
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="icon">
                        <MoreHorizontal className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>

                    <DropdownMenuContent align="end">
                      <DropdownMenuItem>
                        <ArrowRightLeft className="mr-2 h-4 w-4" />
                        Change Position
                      </DropdownMenuItem>

                      <DropdownMenuItem className="text-destructive focus:text-destructive">
                        <Trash2 className="mr-2 h-4 w-4" />
                        Remove Candidate
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
      {candidates.length > itemsPerPage && (
        <div className="flex items-center justify-between pt-4">
          <p className="text-muted-foreground text-sm">
            Showing {page * itemsPerPage + 1}-
            {Math.min((page + 1) * itemsPerPage, candidates.length)} of{" "}
            {candidates.length}
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
                variant={page === i ? "default" : "ghost"}
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

  const bulkUploadButton = (
    <Button variant="outline" onClick={openDialog}>
      <Upload className="mr-2 h-4 w-4" />
      Bulk Upload
    </Button>
  );

  return (
    <>
      <SectionCard
        title="Election Candidates"
        actions={
          candidates.length === 0 ? (
            bulkUploadButton
          ) : (
            <div className="flex gap-2">
              {candidates.length > itemsPerPage && (
                <Button variant="outline" onClick={() => setViewAllOpen(true)}>
                  <Eye className="mr-2 h-4 w-4" />
                  View All ({candidates.length})
                </Button>
              )}
              <Button>
                <Plus className="mr-2 h-4 w-4" />
                Add Candidate
              </Button>
            </div>
          )
        }
      >
        {candidates.length === 0 ? (
          <EmptyState
            title="No candidates found"
            description="Upload candidates in bulk to get started."
            action={bulkUploadButton}
          />
        ) : (
          content
        )}
      </SectionCard>

      <ViewAllDialog
        open={viewAllOpen}
        onOpenChange={(open) => {
          setViewAllOpen(open);
          if (!open) setViewAllSearch("");
        }}
        title="All Candidates"
        totalCount={filteredCandidates.length}
        searchValue={viewAllSearch}
        onSearchChange={setViewAllSearch}
        searchPlaceholder="Search by name, email or position..."
      >
        {(displayCount) => (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Position</TableHead>
                <TableHead className="w-30 text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredCandidates.slice(0, displayCount).map((candidate) => (
                <TableRow key={candidate.id}>
                  <TableCell className="font-medium">
                    {candidate.fullName}
                  </TableCell>
                  <TableCell>{candidate.email}</TableCell>
                  <TableCell>
                    <Badge variant="secondary">
                      {positions.find(
                        (p) => p.electionPositionId === candidate.positionId
                      )?.positionName ?? "-"}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right">
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon">
                          <MoreHorizontal className="h-4 w-4" />
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem>
                          <ArrowRightLeft className="mr-2 h-4 w-4" />
                          Change Position
                        </DropdownMenuItem>
                        <DropdownMenuItem className="text-destructive focus:text-destructive">
                          <Trash2 className="mr-2 h-4 w-4" />
                          Remove Candidate
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
            <DialogTitle>Bulk Upload Candidates</DialogTitle>
            {step === "schema" ? (
              <DialogDescription>
                Set the number of candidates for each position (minimum required
                shown), then download the CSV schema. Fill it in and upload it
                back.
              </DialogDescription>
            ) : (
              <DialogDescription>
                {hasExistingTemplate
                  ? "Upload your CSV file to add candidates."
                  : "Schema downloaded. Upload the filled CSV file to add candidates."}
              </DialogDescription>
            )}
          </DialogHeader>

          {step === "schema" ? (
            <>
              <div className="space-y-3">
                {positions.map((position) => {
                  const minRequired = position.maxSelection;
                  const currentCount = counts[position.electionPositionId];
                  const isBelowMin =
                    currentCount !== undefined && currentCount < minRequired;

                  return (
                    <div
                      key={position.electionPositionId}
                      className="flex items-center gap-3"
                    >
                      <Input
                        value={position.positionName}
                        disabled
                        className="flex-1"
                      />
                      <div className="flex items-center gap-1.5">
                        <Input
                          type="number"
                          min={minRequired}
                          value={currentCount ?? ""}
                          onChange={(e) =>
                            handleCountChange(
                              position.electionPositionId,
                              e.target.value
                            )
                          }
                          className="w-20 text-center"
                          placeholder="0"
                        />
                        {isBelowMin && (
                          <span className="text-destructive text-xs leading-none whitespace-nowrap">
                            Min {minRequired}
                          </span>
                        )}
                        {!isBelowMin && currentCount !== undefined && (
                          <span className="text-muted-foreground text-xs leading-none whitespace-nowrap">
                            Min {minRequired}
                          </span>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>

              <DialogFooter className="sm:justify-between">
                <Button
                  variant="outline"
                  onClick={() => {
                    setStep("upload");
                    setHasExistingTemplate(true);
                  }}
                >
                  Already have template
                </Button>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    onClick={() => setDialogOpen(false)}
                  >
                    Cancel
                  </Button>
                  <Button
                    onClick={handleCreateSchema}
                    disabled={hasError || downloadSchema.isPending}
                  >
                    {downloadSchema.isPending ? (
                      "Downloading..."
                    ) : (
                      <>
                        <FileDown className="mr-2 h-4 w-4" />
                        Create Bulk Schema
                      </>
                    )}
                  </Button>
                </div>
              </DialogFooter>
            </>
          ) : (
            <>
              {hasExistingTemplate ? (
                <div className="text-muted-foreground flex items-center gap-2 rounded-lg border p-3 text-sm">
                  <Upload className="h-4 w-4 shrink-0" />
                  Upload your CSV file with candidate data.
                </div>
              ) : (
                <div className="flex items-center gap-2 rounded-lg border border-green-200 bg-green-50 p-3 text-sm text-green-700 dark:border-green-800 dark:bg-green-950 dark:text-green-400">
                  <CheckCircle2 className="h-4 w-4 shrink-0" />
                  Schema downloaded successfully. Fill in the CSV and upload it
                  below.
                </div>
              )}

              {validationErrors && (
                <div className="space-y-1 rounded-lg border border-red-200 bg-red-50 p-3 text-sm text-red-700 dark:border-red-800 dark:bg-red-950 dark:text-red-400">
                  <p className="font-medium">Validation errors:</p>
                  <ul className="list-inside list-disc space-y-0.5">
                    {validationErrors.map((err, i) => (
                      <li key={i}>{err}</li>
                    ))}
                  </ul>
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
                    setValidationErrors(null);
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
                      Upload Candidates
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
