import { Component } from '@angular/core';
import { NoteCardFormComponent } from './components/note-card-form/note-card-form.component';

@Component({
  selector: 'app-notes-page',
  standalone: true,
  imports: [NoteCardFormComponent],
  templateUrl: './notes-page.component.html',
  styleUrl: './notes-page.component.css',
})
export class NotesPageComponent {}
