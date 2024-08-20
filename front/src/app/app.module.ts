import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app.routes'; 
import { AppComponent } from './app.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FaqComponent } from './pages/support-page/support/faq/faq.component'; 
import { FormsModule } from '@angular/forms';
@NgModule({
    declarations: [
        AppComponent,
        CartPageComponent, 
        FaqComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FormsModule
    ],
   
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
