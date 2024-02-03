import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

const errorMessages = {
  hasEightCharacters: 'Password must be at least 8 characters long',
  hasUpperCase: 'Password must contain at least one uppercase letter',
  hasLowerCase: 'Password must contain at least one lowercase letter',
  hasDigit: 'Password must contain at least one digit',
  hasSpecialChar:
    "Password must contain at least one special character from '!@#$%^&*(),.?\":{}|<>'",
};

function generateValidationErrors(
  hasEightCharacters: boolean,
  hasUpperCase: boolean,
  hasLowerCase: boolean,
  hasDigit: boolean,
  hasSpecialChar: boolean,
): ValidationErrors {
  const validationErrors: ValidationErrors = {};

  if (!hasEightCharacters) {
    validationErrors['hasEightCharacters'] = errorMessages.hasEightCharacters;
  }

  if (!hasUpperCase) {
    validationErrors['hasUpperCase'] = errorMessages.hasUpperCase;
  }

  if (!hasLowerCase) {
    validationErrors['hasLowerCase'] = errorMessages.hasLowerCase;
  }

  if (!hasDigit) {
    validationErrors['hasDigit'] = errorMessages.hasDigit;
  }

  if (!hasSpecialChar) {
    validationErrors['hasSpecialChar'] = errorMessages.hasSpecialChar;
  }

  return validationErrors;
}

export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const password: string = control.value;

    if (!password) {
      return null;
    }

    const hasEightCharacters = password.length >= 8;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasDigit = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    const validationErrors = generateValidationErrors(
      hasEightCharacters,
      hasUpperCase,
      hasLowerCase,
      hasDigit,
      hasSpecialChar,
    );

    const isValid: boolean =
      hasEightCharacters &&
      hasUpperCase &&
      hasLowerCase &&
      hasDigit &&
      hasSpecialChar;

    console.log({ isValid, validationErrors });

    return !isValid ? validationErrors : null;
  };
}
