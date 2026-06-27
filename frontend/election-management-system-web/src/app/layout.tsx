import type { Metadata, Viewport } from "next";
import { Geist, Geist_Mono } from "next/font/google";

import "./globals.css";

import { ThemeProvider } from "@/components/providers/theme-provider";
import { QueryProvider } from "@/components/providers/query-provider";

import { Toaster } from "@/components/ui/sonner";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
  display: "swap",
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
  display: "swap",
});

export const metadata: Metadata = {
  metadataBase: new URL("http://localhost:3000"),

  title: {
    default: "Election Management System",
    template: "%s | Election Management System",
  },

  description:
    "Enterprise Election Management System for managing elections, candidates, voters and secure voting.",

  applicationName: "Election Management System",

  keywords: [
    "Election",
    "Voting",
    "Election Management",
    "Spring Boot",
    "Next.js",
    "Admin Dashboard",
  ],

  authors: [
    {
      name: "Election Management System",
    },
  ],

  creator: "Election Management System",

  publisher: "Election Management System",

  robots: {
    index: true,
    follow: true,
  },
};

export const viewport: Viewport = {
  width: "device-width",
  initialScale: 1,
  themeColor: [
    {
      media: "(prefers-color-scheme: light)",
      color: "#ffffff",
    },
    {
      media: "(prefers-color-scheme: dark)",
      color: "#09090b",
    },
  ],
};

interface RootLayoutProps {
  children: React.ReactNode;
}

export default function RootLayout({ children }: Readonly<RootLayoutProps>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body
        className={`${geistSans.variable} ${geistMono.variable} font-sans antialiased`}
      >
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          <QueryProvider>
            {children}

            <Toaster
              position="top-right"
              richColors
              expand
              closeButton
              visibleToasts={5}
              duration={3000}
            />
          </QueryProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
