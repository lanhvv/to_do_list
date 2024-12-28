import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { NotesListComponent } from "./pages/notes-list/notes-list.component";
import { MainLayoutComponent } from "./pages/main-layout/main-layout.component";
import { NoteDetailComponent } from "./pages/note-detail/note-detail.component";

const routes: Routes = [
    {
        path: "note_mate", component: MainLayoutComponent, children :[
            {path: "list", component: NotesListComponent},
            {path: "new", component: NoteDetailComponent},
            {path: "update", component: NoteDetailComponent}
        ]
    }
]

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}