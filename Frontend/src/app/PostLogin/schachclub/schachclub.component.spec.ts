import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchachclubComponent } from './schachclub.component';

describe('SchachclubComponent', () => {
  let component: SchachclubComponent;
  let fixture: ComponentFixture<SchachclubComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SchachclubComponent]
    });
    fixture = TestBed.createComponent(SchachclubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
