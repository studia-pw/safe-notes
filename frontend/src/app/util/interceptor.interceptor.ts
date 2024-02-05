import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpStatusCode,
} from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from '../services/auth-service.service';

export const interceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthServiceService);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const eventResponseCode = error.status;
      if (eventResponseCode === HttpStatusCode.Unauthorized) {
        // window.location.href = '/login';
        router.navigate(['/login']);
      }

      return throwError(error);
    }),
  );
};
