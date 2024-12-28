import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-note-detail',
  templateUrl: './note-detail.component.html',
  styleUrls: ['./note-detail.component.css']
})
export class NoteDetailComponent {
  public formNote = new FormGroup({
    id: new FormControl(0),
    title: new FormControl("", [Validators.required, Validators.maxLength(100)]),
    body: new FormControl("")
  });
  
  constructor () {}

  public get(name:string) {
    return this.formNote.get(name);
  }
}
