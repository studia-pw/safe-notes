import { Component } from '@angular/core';
import { NoteCardFormComponent } from './components/note-card-form/note-card-form.component';
import { CardViewComponent } from './components/card-view/card-view.component';
import { Note } from '../dto/note';
import { NoteServiceService } from '../services/note-service.service';
import { NgForOf } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatTabsModule } from '@angular/material/tabs';
import { AuthServiceService } from '../services/auth-service.service';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-notes-page',
  standalone: true,
  imports: [
    NoteCardFormComponent,
    CardViewComponent,
    NgForOf,
    MatListModule,
    MatTabsModule,
    MatButtonModule,
  ],
  templateUrl: './notes-page.component.html',
  styleUrl: './notes-page.component.css',
})
export class NotesPageComponent {
  notes!: Note[];
  publicNotes!: Note[];
  loggedUser = this.auth.getUser();

  constructor(
    private noteService: NoteServiceService,
    private auth: AuthServiceService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.noteService
      .getUserNotes(this.loggedUser?.id ?? -1)
      .subscribe((notes) => {
        this.notes = notes;
      });

    this.noteService.getPublicNotes().subscribe((notes) => {
      this.publicNotes = notes;
    });
  }

  updateNotes() {
    this.noteService
      .getUserNotes(this.loggedUser?.id ?? -1)
      .subscribe((notes) => {
        this.notes = notes;
      });

    this.noteService.getPublicNotes().subscribe((notes) => {
      this.publicNotes = notes;
    });
  }

  onLogoutClicked() {
    this.auth.logout().subscribe(
      (response) => {
        this.auth.setUser(null);
        this.router.navigate(['/login']);
      },
      (error) => {
        console.log(error);
      },
    );
  }
}
