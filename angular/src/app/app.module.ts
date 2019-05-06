import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {
  HTTP_INTERCEPTORS,
  HttpClientModule
} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {routing} from './app.routing';


import {AppComponent} from './app.component';
import {ApiService} from './common/api.service';
import {ErrorInterceptor} from './common/error.interceptor';
import {JwtInterceptor} from './common/jwt.interceptor';
import {LoginComponent} from './login/login.component';
import {AddFileComponent} from './add-file/add-file.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { ProgressComponent } from './progress/progress.component';
import { ListFilesComponent } from './list-files/list-files.component';
import { ShareFileComponent } from './share-file/share-file.component';
import { DownloadFileComponent } from './download-file/download-file.component';
import { RegisterComponent } from './register/register.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AddFileComponent,
    FileUploadComponent,
    ProgressComponent,
    ListFilesComponent,
    ShareFileComponent,
    DownloadFileComponent,
    RegisterComponent
  ],
  imports: [
    BrowserModule,
    routing,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [ApiService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
