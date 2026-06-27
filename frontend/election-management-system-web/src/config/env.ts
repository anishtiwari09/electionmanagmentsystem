function getEnvironmentVariable(key: string): string {
  const value = process.env[key];

  if (!value) {
    throw new Error(`Missing required environment variable: ${key}`);
  }

  return value;
}

export const env = {
  API_BASE_URL: getEnvironmentVariable("NEXT_PUBLIC_API_BASE_URL"),
} as const;
