import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { routing } from './app.routing'
import { AppComponent } from './app.component';

import { AppHeader } from './components/header.component';
import { AppFooter } from './components/footer.component';
import { AppShowText } from './components/show-text.component';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TextPage } from './components/text-page.component';
import { TextForm } from './components/text-form.component';
import { Flashcards } from './components/flashcards.component';
import { FlashcardPage } from './components/flashcard.component';
import { UserForm } from './components/user-form.component';
import { Profile } from './components/profile.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    AppHeader,
    AppFooter,
    AppShowText,
    TextPage,
    TextForm,
    Flashcards,
    FlashcardPage,
    UserForm,
    Profile
  ],
  imports: [
    BrowserModule,
    routing,
    FormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
