"use client";

import { format } from "date-fns";
import { CalendarIcon } from "lucide-react";
import { useState } from "react";

import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { cn } from "@/lib/utils";

interface DateTimePickerProps {
  value?: Date;
  onChange: (date?: Date) => void;
}

export function DateTimePicker({
  value,
  onChange,
}: Readonly<DateTimePickerProps>) {
  const [date, setDate] = useState<Date | undefined>(value);
  const [open, setOpen] = useState(false);

  const updateHours = (hours: string) => {
    if (!date) return;

    const next = new Date(date);
    next.setHours(Number(hours));

    setDate(next);
    onChange(next);
  };

  const updateMinutes = (minutes: string) => {
    if (!date) return;

    const next = new Date(date);
    next.setMinutes(Number(minutes));

    setDate(next);
    onChange(next);
  };

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          className={cn(
            "w-full justify-start text-left font-normal",
            !date && "text-muted-foreground"
          )}
        >
          <CalendarIcon className="mr-2 h-4 w-4" />

          {date ? format(date, "PPP p") : "Select date & time"}
        </Button>
      </PopoverTrigger>

      <PopoverContent className="w-auto space-y-4 p-4">
        <Calendar
          mode="single"
          selected={date}
          onSelect={(selectedDate) => {
            if (!selectedDate) return;

            const next = new Date(selectedDate);

            if (date) {
              next.setHours(date.getHours());
              next.setMinutes(date.getMinutes());
            }

            setDate(next);
            onChange(next);
          }}
        />

        <div className="flex gap-3">
          <Select
            value={String(date?.getHours() ?? 0)}
            onValueChange={updateHours}
          >
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>

            <SelectContent>
              {Array.from({ length: 24 }).map((_, hour) => (
                <SelectItem key={hour} value={String(hour)}>
                  {hour.toString().padStart(2, "0")}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>

          <Select
            value={String(date?.getMinutes() ?? 0)}
            onValueChange={updateMinutes}
          >
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>

            <SelectContent>
              {Array.from({ length: 60 }).map((_, minute) => (
                <SelectItem key={minute} value={String(minute)}>
                  {minute.toString().padStart(2, "0")}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <div className="flex justify-end">
          {date && (
            <Button
              size="sm"
              onClick={() => {
                if (date) {
                  onChange(date);
                }
                setOpen(false);
              }}
            >
              Apply
            </Button>
          )}
        </div>
      </PopoverContent>
    </Popover>
  );
}
