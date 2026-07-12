import { useMemo, useState } from "react";

const DEFAULT_ITEMS_PER_PAGE = 5;

export function usePagination<T>(
  items: T[],
  itemsPerPage: number = DEFAULT_ITEMS_PER_PAGE
) {
  const [page, setPage] = useState(0);
  const totalPages = Math.max(1, Math.ceil(items.length / itemsPerPage));
  const safePage = Math.min(page, totalPages - 1);

  const paginatedItems = useMemo(
    () => items.slice(safePage * itemsPerPage, (safePage + 1) * itemsPerPage),
    [items, safePage, itemsPerPage]
  );

  return {
    page: safePage,
    setPage,
    totalPages,
    paginatedItems,
    itemsPerPage,
  };
}
