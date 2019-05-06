import {
  Component,
  Input,
  OnInit
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import * as fileSaver from 'file-saver';
import {ApiService} from '../common/api.service';

@Component({
  selector: 'app-download-file',
  templateUrl: './download-file.component.html',
  styleUrls: ['./download-file.component.css']
})
export class DownloadFileComponent implements OnInit {

  @Input() file;
  downloadForm = new FormGroup({
    secret: new FormControl(null, Validators.required)
  });
  downloadError : boolean;
  constructor(private apiService: ApiService) {
  }

  ngOnInit() {

  }

  hasError(field: string, error: string) {
    const control = this.downloadForm.get(field);
    return control.dirty && control.hasError(error);
  }

  submit() {
    this.apiService.downloadFile(this.file.fileId, this.downloadForm.controls['secret'].value)
      .subscribe(response  => {
        this.saveFile(response.body, this.file.name);
        this.downloadError = false;
      }, (error) => {
        this.downloadError = true;
      });


  }




  saveFile(data: any, filename?: string) {
    const blob = new Blob([data], {type: 'text/csv; charset=utf-8'});
    fileSaver.saveAs(blob, filename);
  }
}
