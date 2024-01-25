import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateNote } from '../dto/create-note';
import { Note } from '../dto/note';

@Injectable({
  providedIn: 'root',
})
export class NoteServiceService {
  baseUrl = 'http://localhost:8080/api/notes';

  constructor(private http: HttpClient) {}

  createNote(note: CreateNote) {
    return this.http.post(this.baseUrl, note, { withCredentials: true });
  }

  getUserNotes(userId: Number) {
    return this.http.get<Note[]>(this.baseUrl + `/${userId}`, {
      withCredentials: true,
    });
  }

  getPublicNotes() {
    return this.http.get<Note[]>(this.baseUrl, {
      withCredentials: true,
    });
  }

  decryptNote(password: string, noteId: Number) {
    return this.http.post<Note>(
      this.baseUrl + `/private/${noteId}`,
      { password },
      {
        withCredentials: true,
      },
    );
  }
}
