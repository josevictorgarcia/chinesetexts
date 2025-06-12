import { Routes, RouterModule } from "@angular/router";
import { AppShowText } from "./components/show-text.component";
import { TextPage } from "./components/text-page.component";
import { TextForm } from "./components/text-form.component";
import { Flashcards } from "./components/flashcards.component";
import { FlashcardPage } from "./components/flashcard.component";
import { UserForm } from "./components/user-form.component";
import { Profile } from "./components/profile.component";

const appRoutes: Routes = [
  //{ path: "courses", component: CourseListComponent },
  { path: '', component: AppShowText},
  { path: 'texts/new', component: TextForm},
  { path: 'texts/:id', component: TextPage},
  { path: 'flashcards', component: Flashcards },
  { path: 'flashcards/:id', component: FlashcardPage},
  { path: 'users/new', component: UserForm},
  { path: 'users/me', component: Profile},
  { path: '', redirectTo: '', pathMatch: 'full' }
];

export const routing = RouterModule.forRoot(appRoutes);