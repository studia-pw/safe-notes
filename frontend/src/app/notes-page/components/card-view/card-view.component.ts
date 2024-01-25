import { Component, Input, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Note } from '../../../dto/note';
import { MatButtonModule } from '@angular/material/button';
import { SanitizerHtmlPipe } from '../../../util/sanitizer-html.pipe';
import { NgIf } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoteServiceService } from '../../../services/note-service.service';

@Component({
  selector: 'app-card-view',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    SanitizerHtmlPipe,
    NgIf,
    FormsModule,
    MatInputModule,
    ReactiveFormsModule,
  ],
  templateUrl: './card-view.component.html',
  styleUrl: './card-view.component.css',
})
export class CardViewComponent implements OnInit {
  @Input() note!: Note;
  form!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private noteService: NoteServiceService,
  ) {}

  ngOnInit() {
    this.form = this.formBuilder.group({
      notePassword: ['', Validators.required],
    });
  }

  onDecrypt() {
    let password = this.form.get('notePassword')?.value;
    this.noteService.decryptNote(password, this.note.id).subscribe((req) => {
      this.note.content = req.content;
    });
  }
}
