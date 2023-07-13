import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateImageFormComponent } from './create-image-form.component';

describe('CreateImageFormComponent', () => {
  let component: CreateImageFormComponent;
  let fixture: ComponentFixture<CreateImageFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateImageFormComponent]
    });
    fixture = TestBed.createComponent(CreateImageFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
