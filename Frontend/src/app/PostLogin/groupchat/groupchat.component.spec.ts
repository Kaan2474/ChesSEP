import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupchatComponent } from './groupchat.component';

describe('GroupchatComponent', () => {
  let component: GroupchatComponent;
  let fixture: ComponentFixture<GroupchatComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GroupchatComponent]
    });
    fixture = TestBed.createComponent(GroupchatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
