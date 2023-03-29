import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { firstValueFrom } from 'rxjs';
import { UploadResult } from '../model/upload-result';

@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  constructor(private httpClient: HttpClient) { }

  getImage(postId: string) {
    return firstValueFrom(
      this.httpClient.get<UploadResult>('/get-image/' + postId)
    );
  }
}