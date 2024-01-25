import { Component } from '@angular/core';
import { NoteCardFormComponent } from './components/note-card-form/note-card-form.component';
import { CardViewComponent } from './components/card-view/card-view.component';
import { Note } from '../dto/note';
import { NoteServiceService } from '../services/note-service.service';
import { NgForOf } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-notes-page',
  standalone: true,
  imports: [
    NoteCardFormComponent,
    CardViewComponent,
    NgForOf,
    MatListModule,
    MatTabsModule,
  ],
  templateUrl: './notes-page.component.html',
  styleUrl: './notes-page.component.css',
})
export class NotesPageComponent {
  notes!: Note[];
  publicNotes!: Note[];

  constructor(private noteService: NoteServiceService) {}

  ngOnInit() {
    this.noteService.getUserNotes(1).subscribe((notes) => {
      this.notes = notes;
    });

    this.noteService.getPublicNotes().subscribe((notes) => {
      this.publicNotes = notes;
    });
  }

  updateNotes() {
    this.noteService.getUserNotes(1).subscribe((notes) => {
      this.notes = notes;
    });

    this.noteService.getPublicNotes().subscribe((notes) => {
      this.publicNotes = notes;
    });
  }
}
