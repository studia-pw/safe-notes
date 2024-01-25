import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpStatusCode,
} from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const interceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const eventResponseCode = error.status;
      if (
        eventResponseCode === HttpStatusCode.Forbidden ||
        eventResponseCode === HttpStatusCode.Unauthorized
      ) {
        // window.location.href = '/login';
        router.navigate(['/login']);
      }

      return throwError(error);
    }),
  );
};
