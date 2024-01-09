import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StreamingAnsichtComponent } from './streaming-ansicht.component';

describe('StreamingAnsichtComponent', () => {
  let component: StreamingAnsichtComponent;
  let fixture: ComponentFixture<StreamingAnsichtComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StreamingAnsichtComponent]
    });
    fixture = TestBed.createComponent(StreamingAnsichtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
