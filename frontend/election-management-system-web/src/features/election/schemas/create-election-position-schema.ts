import { z } from "zod";

export const positionItemSchema = z.object({
  positionName: z
    .string()
    .trim()
    .min(1, "Position name is required.")
    .max(100, "Position name cannot exceed 100 characters.")
    .regex(
      /^[a-zA-Z0-9 ]+$/,
      "Only letters and numbers are allowed (no special characters)."
    ),

  description: z
    .string()
    .trim()
    .max(500, "Description cannot exceed 500 characters."),

  minSelection: z.number().min(0, "Minimum selection must be at least 0."),

  maxSelection: z.number().min(1, "Maximum selection must be at least 1."),
});

export const createElectionPositionSchema = z
  .object({
    positions: z.array(positionItemSchema).min(1, "Add at least one position."),
  })
  .superRefine(({ positions }, ctx) => {
    const names = new Set<string>();

    positions.forEach((position, index) => {
      const normalized = position.positionName.trim().toLowerCase();

      if (names.has(normalized)) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: "Position name already exists.",
          path: ["positions", index, "positionName"],
        });
      }

      names.add(normalized);

      if (position.maxSelection < position.minSelection) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message:
            "Maximum selection must be greater than or equal to minimum selection.",
          path: ["positions", index, "maxSelection"],
        });
      }
    });
  });

export type CreateElectionPositionFormData = z.infer<
  typeof createElectionPositionSchema
>;
