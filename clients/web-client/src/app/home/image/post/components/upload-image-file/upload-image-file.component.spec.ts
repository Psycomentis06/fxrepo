import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadImageFileComponent } from './upload-image-file.component';

describe('UploadImageFileComponent', () => {
  let component: UploadImageFileComponent;
  let fixture: ComponentFixture<UploadImageFileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadImageFileComponent]
    });
    fixture = TestBed.createComponent(UploadImageFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
