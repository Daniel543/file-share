import {
  RouterModule,
  Routes
} from '@angular/router';
import {ListFilesComponent} from './list-files/list-files.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {ShareFileComponent} from './share-file/share-file.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'share-file', component: ShareFileComponent},
  {path: '', component: ListFilesComponent},
  {path: 'register', component: RegisterComponent}
];

export const routing = RouterModule.forRoot(routes);
