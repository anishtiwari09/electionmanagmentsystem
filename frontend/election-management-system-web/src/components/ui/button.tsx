import * as React from "react";
import { Slot, Slottable } from "@radix-ui/react-slot";
import { Loader2 } from "lucide-react";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "@/lib/utils";

const buttonVariants = cva(
  [
    "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-lg text-sm font-medium transition-all duration-150 ease-out",
    "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
    "disabled:pointer-events-none disabled:opacity-50",
    "[&_svg]:pointer-events-none [&_svg]:shrink-0",
    "[&_svg:not([class*='size-'])]:size-4",
    "active:scale-[0.98]",
    "select-none",
  ].join(" "),
  {
    variants: {
      variant: {
        primary:
          "bg-primary text-primary-foreground shadow-sm hover:bg-primary/90",

        secondary:
          "bg-secondary text-secondary-foreground shadow-sm hover:bg-secondary/80",

        outline:
          "border border-border bg-background shadow-sm hover:bg-accent hover:text-accent-foreground",

        ghost: "hover:bg-accent hover:text-accent-foreground",

        destructive:
          "bg-destructive text-destructive-foreground shadow-sm hover:bg-destructive/90",

        success: "bg-emerald-600 text-white shadow-sm hover:bg-emerald-700",

        warning: "bg-amber-500 text-white shadow-sm hover:bg-amber-600",

        link: "text-primary underline-offset-4 hover:underline",
      },

      size: {
        sm: "h-9 px-3 text-xs",

        md: "h-10 px-4",

        lg: "h-11 px-6",

        icon: "size-10",
      },

      fullWidth: {
        true: "w-full",
        false: "",
      },
    },

    defaultVariants: {
      variant: "primary",
      size: "md",
      fullWidth: false,
    },
  }
);

export interface ButtonProps
  extends
    React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;

  loading?: boolean;

  leftIcon?: React.ReactNode;

  rightIcon?: React.ReactNode;
}

function Button({
  className,
  variant,
  size,
  fullWidth,
  asChild = false,
  loading = false,
  leftIcon,
  rightIcon,
  children,
  disabled,
  ...props
}: Readonly<ButtonProps>) {
  const Component = asChild ? Slot : "button";

  return (
    <Component
      data-slot="button"
      className={cn(
        buttonVariants({
          variant,
          size,
          fullWidth,
        }),
        className
      )}
      disabled={!asChild ? disabled || loading : undefined}
      aria-disabled={asChild ? disabled || loading : undefined}
      {...props}
    >
      {loading ? (
        <Loader2 className="size-4 animate-spin" aria-hidden="true" />
      ) : (
        leftIcon
      )}

      <Slottable>{children}</Slottable>

      {!loading && rightIcon}
    </Component>
  );
}

export { Button, buttonVariants };
