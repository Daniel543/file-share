import {Injectable} from '@angular/core';
import {
  HttpClient,
  HttpEventType
} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {map} from 'rxjs/operators';

@Injectable()
export class ApiService {

  constructor(private http: HttpClient) {
  }

  baseUrl: string = 'http://localhost:8080/';

  login(loginPayload) {
    const headers = {
      'Authorization': 'Basic ' + btoa('fileshare-angular:topDano'),
      'Content-type': 'application/x-www-form-urlencoded'
    };
    return this.http.post(this.baseUrl + 'oauth/token', loginPayload, {headers});// TODO
  }

  /*public upload(data, userId) {
    let uploadURL = `${this.baseUrl}/auth/${userId}/avatar`;

    return this.http.post<any>(uploadURL, data, {
      reportProgress: true,
      observe: 'events'
    }).pipe(map((event) => {

        switch (event.type) {

          case HttpEventType.UploadProgress:
            const progress = Math.round(100 * event.loaded / event.total);
            return { status: 'progress', message: progress };

          case HttpEventType.Response:
            return event.body;
          default:
            return `Unhandled event: ${event.type}`;
        }
      })
    );
  }*/

  getFiles() {
    return this.http.get(this.baseUrl + 'files');
  }

  getFile(id) {
    return this.http.get(this.baseUrl + 'files/' + id);
  }

  deleteFile(id: number) {
    return this.http.delete(this.baseUrl + 'files/' + id);
  }

  downloadFile(id, secret) {
    return this.http.get(this.baseUrl + 'files/download/' + id + '?secret=' + secret, {observe: 'response', responseType: 'blob'});
  }

  getUsersForFile(id) {
    return this.http.get(this.baseUrl + 'files/' + id + '/users');
  }

  removeUserAccesToFile(userId, fileId) {
    return this.http.delete(this.baseUrl + 'files/' + fileId + '/users/' + userId);
  }

  addUserAccessToFile(username, fileId) {
    return this.http.post(this.baseUrl + 'files/' + fileId + '/users', this.toFormData(username));
  }

  register(data){
    return this.http.post(this.baseUrl + 'users', data);
  }

  getSecret(){
    return this.http.get(this.baseUrl + 'files/secret');
  }

  toFormData<T>(formValue: T) {
    const formData = new FormData();

    for (const key of Object.keys(formValue)) {
      const value = formValue[key];
      formData.append(key, value);
    }

    return formData;
  }
}
