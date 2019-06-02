import { Component } from '@angular/core';
import {AuthService} from './common/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'FileshareApp';

  constructor(private authService: AuthService) {
  }
  isUserLoggedIn(){
    return this.authService.isLoggedIn();
  }
}
