import {
  HttpClient,
  HttpEvent,
  HttpEventType,
  HttpResponse
} from '@angular/common/http';
import {
  Component,
  EventEmitter,
  OnInit,
  Output
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import {pipe} from 'rxjs';
import {
  filter,
  map,
  tap
} from 'rxjs/operators';
import {ApiService} from '../common/api.service';

@Component({
  selector: 'app-add-file',
  templateUrl: './add-file.component.html',
  styleUrls: ['./add-file.component.css']
})


export class AddFileComponent implements OnInit {
  @Output() fileUploaded = new EventEmitter();
  progress = 0;
  fileAdd = new FormGroup({
    secret: new FormControl(null, Validators.required),
    file: new FormControl(null, [Validators.required])
  });
  success = false;
  secret: any;

  constructor(private http: HttpClient, private apiService: ApiService) {
  }

  ngOnInit() {
    this.apiService.getSecret()
      .subscribe(data => {
        this.secret = data;
        this.fileAdd.controls['secret'].setValue(this.secret.value);
      });

  }

  submit() {
    this.success = false;
    if (!this.fileAdd.valid) {
      markAllAsDirty(this.fileAdd);
      return;
    }

    this.http.post('http://localhost:8080/files/upload', toFormData(this.fileAdd.value), {
      reportProgress: true,
      observe: 'events'
    }).pipe(
      uploadProgress(progress => (this.progress = progress)),
      toResponseBody()
    ).subscribe(res => {
      this.progress = 0;
      this.success = true;
      this.fileAdd.reset();
      this.fileUploaded.emit();
    });
  }

  hasError(field: string, error: string) {
    const control = this.fileAdd.get(field);
    return control.dirty && control.hasError(error);
  }
}

export function uploadProgress<T>(cb: (progress: number) => void) {
  return tap((event: HttpEvent<T>) => {
    if (event.type === HttpEventType.UploadProgress) {
      cb(Math.round((100 * event.loaded) / event.total));
    }
  });
}

export function toResponseBody<T>() {
  return pipe(
    filter((event: HttpEvent<T>) => event.type === HttpEventType.Response),
    map((res: HttpResponse<T>) => res.body)
  );
}

export function markAllAsDirty(form: FormGroup) {
  for (const control of Object.keys(form.controls)) {
    form.controls[control].markAsDirty();
  }
}

export function toFormData<T>(formValue: T) {
  const formData = new FormData();

  for (const key of Object.keys(formValue)) {
    const value = formValue[key];
    formData.append(key, value);
  }

  return formData;
}
