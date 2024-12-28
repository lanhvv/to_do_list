import { AfterViewInit, Component, ElementRef, Input, OnInit, Renderer2, ViewChild } from '@angular/core';

@Component({
  selector: 'app-note-card',
  templateUrl: './note-card.component.html',
  styleUrls: ['./note-card.component.css']
})
export class NoteCardComponent implements OnInit {
  @Input('title') title !: String;
  @Input('body') body !: String;
  @ViewChild('truncator') truncator !: ElementRef<HTMLElement>;
  @ViewChild('bodyText') bodyText !: ElementRef<HTMLElement>;

  constructor(private renderer: Renderer2) {}


  ngOnInit(): void {
  }
}
