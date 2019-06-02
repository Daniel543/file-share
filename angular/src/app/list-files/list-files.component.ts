import {
  Component,
  OnInit
} from '@angular/core';
import {Router} from '@angular/router';
import {ApiService} from '../common/api.service';
import {AuthService} from '../common/auth.service';

@Component({
  selector: 'app-list-files',
  templateUrl: './list-files.component.html',
  styleUrls: ['./list-files.component.css']
})
export class ListFilesComponent implements OnInit {

  files: any;
  fileToDownload: any;

  constructor(private router: Router, private apiService: ApiService, private authService: AuthService) {
  }

  ngOnInit() {

    /*if (!window.sessionStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }*/
    if (!this.authService.isLoggedIn()){
      this.router.navigate(['login']);
      return;
    }
    this.refreshFiles();

  }

  refreshFiles(): void {
    this.apiService.getFiles()
      .subscribe(data => {
        console.log(data);
        this.files = data;
      });
  }

  deleteFile(file): void {
    this.apiService.deleteFile(file.fileId)
      .subscribe(data => {
        this.files = this.files.filter(u => u !== file);
      });
  }

  setFileToDownload(file): void{
    this.fileToDownload = file;
  }


  shareFile(file): void {
    window.sessionStorage.removeItem("shareFileId");
    window.sessionStorage.setItem("shareFileId", file.fileId.toString());
    this.router.navigate(['share-file']);
  }
}
