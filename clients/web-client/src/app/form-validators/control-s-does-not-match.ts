import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export function ControlSDoesNotMatch(controlName1: string, controlName2: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const control1 = control.get(controlName1)
        const control2 = control.get(controlName2)
        if (control1 && control2) {
            return control1.value !== control2.value ? {mismatch: true} : null
        }
        return null
    }
}
