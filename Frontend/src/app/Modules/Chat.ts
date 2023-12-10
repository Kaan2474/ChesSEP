
export class Chat {
  ownerId:any;
  senderId:any;
  senderName:any;
  recipientId:any;
  chessClubId:any;
  chessClubName:any;
  privateGroupName:any;
  user:any[]=[];
  content:any;
  chatId:any;
  chatMessageStatus:any;
  newContent:any;
  oldContent:any;
  time: any;
  messageId: MessageId = new MessageId();  // Neues Attribut für messageId
}
export class MessageId {
  senderId: any;
  chatId: any;
  time: any;
}
