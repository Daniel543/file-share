import {HttpParams} from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import {Router} from '@angular/router';
import {toFormData} from '../add-file/add-file.component';
import {ApiService} from '../common/api.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm = new FormGroup({
    username: new FormControl(null, [Validators.required, Validators.pattern('[a-zA-Z0-9]*')]),
    email: new FormControl(null, [Validators.required, Validators.email]),
    password: new FormControl(null, Validators.required)
  });
  successful: boolean;
  error: boolean;


  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiService) { }

  ngOnInit() {
  }

  hasError(field: string, error: string) {
    const control = this.registerForm.get(field);
    return control.dirty && control.hasError(error);
  }

  submit() {
    const body = new HttpParams()
      .set('username', this.registerForm.controls['username'].value)
      .set('password', this.registerForm.controls['password'].value)
      .set('email', this.registerForm.controls['email'].value);

    this.apiService.register(body)
      .subscribe(data => {
        this.successful = true;
        this.error = false;
      }, (error) => {
        this.successful = false;
        this.error = true;
      });
  }
}
