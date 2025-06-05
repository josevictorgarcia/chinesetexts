import { Routes, RouterModule } from "@angular/router";
import { AppShowText } from "./components/show-text.component";
import { TextPage } from "./components/text-page.component";
import { TextForm } from "./components/text-form.component";
import { Flashcards } from "./components/flashcards.component";
import { FlashcardPage } from "./components/flashcard.component";

const appRoutes: Routes = [
  //{ path: "courses", component: CourseListComponent },
  { path: '', component: AppShowText},
  { path: 'texts/new', component: TextForm},
  { path: 'texts/:id', component: TextPage},
  { path: 'flashcards', component: Flashcards },
  { path: 'flashcards/:id', component: FlashcardPage},
  { path: '', redirectTo: '', pathMatch: 'full' }
];

export const routing = RouterModule.forRoot(appRoutes);