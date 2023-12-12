import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMembersComponent } from './add-members.component';

describe('AddMembersComponent', () => {
  let component: AddMembersComponent;
  let fixture: ComponentFixture<AddMembersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddMembersComponent]
    });
    fixture = TestBed.createComponent(AddMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
