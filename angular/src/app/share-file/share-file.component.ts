import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import {Router} from '@angular/router';
import {ApiService} from '../common/api.service';


@Component({
  selector: 'app-share-file',
  templateUrl: './share-file.component.html',
  styleUrls: ['./share-file.component.css']
})
export class ShareFileComponent implements OnInit {

  shareForm = new FormGroup({
    username: new FormControl(null, Validators.required)
  });
  users: any;
  fileId: string;
  userNotFound : boolean;
  file: any;

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiService) { }
  ngOnInit() {
    if (!window.sessionStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    let fileId = window.sessionStorage.getItem("shareFileId");
    if(!fileId) {
      alert("Action not permitted.")
      this.router.navigate(['']);
      return;
    }
    this.fileId = fileId;
    this.apiService.getFile(this.fileId)
      .subscribe(data => {
      this.file = data;
    });

    this.refreshUsers();
  }

  hasError(field: string, error: string) {
    const control = this.shareForm.get(field);
    return control.dirty && control.hasError(error);
  }


    refreshUsers(): void {
    this.apiService.getUsersForFile(this.fileId)
      .subscribe(data => {
        console.log(data);
        this.users = data;
      });
  }

  remove(user): void {
    this.apiService.removeUserAccesToFile(user.userId, this.fileId)
      .subscribe(data => {
        this.users = this.users.filter(u => u !== user);
      });
  }

  submit() {
    this.apiService.addUserAccessToFile(this.shareForm.value, this.fileId)
      .subscribe(data => {
        this.userNotFound = false;
        this.refreshUsers();
      }, (error) => {
        this.userNotFound = true;
        console.log(this.userNotFound);
      });
  }

}
