import { z } from "zod";

export const createOrganizerSchema = z.object({
  email: z.email("Please enter a valid email address.").trim().toLowerCase(),
});

export type CreateOrganizerFormData = z.infer<typeof createOrganizerSchema>;
