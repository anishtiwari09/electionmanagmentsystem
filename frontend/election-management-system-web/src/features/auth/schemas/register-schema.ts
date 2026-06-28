import { z } from "zod";

export const registerSchema = z.object({
  firstName: z
    .string()
    .trim()
    .min(1, "First name is required.")
    .max(50, "First name must be at most 50 characters."),

  lastName: z
    .string()
    .trim()
    .min(1, "Last name is required.")
    .max(50, "Last name must be at most 50 characters."),

  email: z.email("Please enter a valid email address.").trim().toLowerCase(),
});

export type RegisterFormData = z.infer<typeof registerSchema>;
