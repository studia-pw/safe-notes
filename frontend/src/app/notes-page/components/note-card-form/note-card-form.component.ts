import { Component, EventEmitter, Output } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NoteServiceService } from '../../../services/note-service.service';
import { CreateNote } from '../../../dto/create-note';

@Component({
  selector: 'app-note-card-form',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSlideToggleModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './note-card-form.component.html',
  styleUrl: './note-card-form.component.css',
})
export class NoteCardFormComponent {
  noteForm!: FormGroup;
  enryptionToggled = false;
  publicToggled = false;

  @Output() noteCreated = new EventEmitter<void>();

  constructor(
    private noteService: NoteServiceService,
    private formBuilder: FormBuilder,
  ) {}

  ngOnInit() {
    this.noteForm = this.formBuilder.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      password: [{ value: '', disabled: true }, Validators.required],
      isPublic: [false],
      isEncrypted: [false],
    });

    const isPublicControl = this.noteForm.get('isPublic');
    const isEncryptedControl = this.noteForm.get('isEncrypted');

    if (isPublicControl) {
      isPublicControl.valueChanges.subscribe((isPublic) => {
        if (isPublic) {
          this.noteForm.get('isEncrypted')?.setValue(false);
          this.noteForm.get('isEncrypted')?.disable();
        } else {
          this.noteForm.get('isEncrypted')?.enable();
        }
      });
    }

    if (isEncryptedControl) {
      isEncryptedControl.valueChanges.subscribe((isEncrypted) => {
        if (isEncrypted) {
          this.noteForm.get('password')?.enable();
        } else {
          this.noteForm.get('password')?.disable();
        }
      });
    }
  }

  onSaveNoteClicked() {
    let note: CreateNote = {
      title: this.noteForm.get('title')?.value,
      content: this.noteForm.get('content')?.value,
      password: this.noteForm.get('password')?.value,
      isPublic: this.noteForm.get('isPublic')?.value,
      isEncrypted: this.noteForm.get('isEncrypted')?.value,
    };

    console.log(note);
    this.noteService.createNote(note).subscribe((r) => {
      console.log(r);
      this.noteCreated.emit();
    });
  }
}
