import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { CameraService } from '../services/camera.service';

@Component({
  selector: 'app-upload-component',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit{
  imageData = ""
  form!: FormGroup;
  blob!: Blob;

  constructor(private router: Router, private fb: FormBuilder,
      private cameraSvc : CameraService){

  }
  ngOnInit(): void {
      if(!this.cameraSvc.imageData){
        this.router.navigate(['/'])
        return;
      }
      this.imageData = this.cameraSvc.imageData;
      this.form = this.fb.group(
        {
          title: this.fb.control<string>(''),
          complain: this.fb.control<string>(''),
        }
      );
      this.blob = this.dataURItoBlob(this.imageData);
  }

  upload(){
    const formVal = this.form.value;
    this.cameraSvc.upload(formVal, this.blob).then((result)=>{
      this.router.navigate(['/']);
    }).catch(error=> {
      console.error(error);
    })
  }

  dataURItoBlob(dataURI: String){
    var byteString = atob(dataURI.split(',')[1]);
    let mimeString = dataURI.split(',')[0].split(';')[0];
    var ab = new ArrayBuffer(byteString.length)
    var ia = new Uint8Array(ab)
    for(var i =0; i <byteString.length; i++){
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], {type: mimeString});
  }
}