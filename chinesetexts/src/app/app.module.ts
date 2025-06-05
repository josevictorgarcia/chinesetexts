import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { routing } from './app.routing'
import { AppComponent } from './app.component';

import { AppHeader } from './components/header.component';
import { AppFooter } from './components/footer.component';
import { AppShowText } from './components/show-text.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { TextPage } from './components/text-page.component';
import { TextForm } from './components/text-form.component';
import { Flashcards } from './components/flashcards.component';
import { FlashcardPage } from './components/flashcard.component';

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
  ],
  imports: [
    BrowserModule,
    routing,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
