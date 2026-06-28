export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  code: string;
}

export interface ApiErrorResponse {
  success: false;
  data: null;
  message: string;
  code: string;
}
