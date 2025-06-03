import { Routes, RouterModule } from "@angular/router";
import { AppShowText } from "./components/show-text.component";
import { TextPage } from "./components/text-page.component";
import { TextForm } from "./components/text-form.component";

const appRoutes: Routes = [
  //{ path: "courses", component: CourseListComponent },
  { path: '', component: AppShowText},
  { path: 'texts/new', component: TextForm},
  { path: 'texts/:id', component: TextPage},
  { path: '', redirectTo: '', pathMatch: 'full' }
];

export const routing = RouterModule.forRoot(appRoutes);