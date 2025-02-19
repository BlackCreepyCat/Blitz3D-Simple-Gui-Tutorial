Graphics(1024, 768, 0, 2)

;MOUSE
   Global GUI_MousePosX, GUI_MousePosY
   Global GUI_MouseKey1, GUI_MouseKey2
   
   Function GUI_MouseUpdate()
      GUI_MousePosX = MouseX()
      GUI_MousePosY = MouseY()
      
      GUI_MouseKey1 = MouseHit(1)
      GUI_MouseKey2 = MouseHit(2)
      
      SetBuffer(BackBuffer())
      Rect(GUI_MousePosX, GUI_MousePosY, 2, 2, 1)
   End Function

;FONTS
   Global FONT_HeadLine = LoadFont("Trebuchet MS", 17, 1, 0, 0)

;WINDOW
   Type GUI_Window
      Field Name$
      Field PosX, PosY
      Field SizeX, SizeY
      Field Buffer, WinMask
   End Type
   Global GUI_W.GUI_Window
   Global GUI_LastWindow.GUI_Window
   
   Global GUI_WindowDropX, GUI_WindowDropY, GUI_WindowDrop
   
   Function GUI_WindowCreate.GUI_Window(Name$, PosX, PosY, SizeX, SizeY)
      GUI_W = New GUI_Window
      GUI_W\Name = Name
      GUI_W\PosX = PosX
      GUI_W\PosY = PosY
      GUI_W\SizeX = SizeX
      GUI_W\SizeY = SizeY
   
      GUI_W\Buffer = CreateImage(SizeX, SizeY)
      GUI_W\WinMask = CreateImage(SizeX, SizeY)
      
      MaskImage(GUI_W\WinMask, 255, 0, 128)
      
      SetBuffer(ImageBuffer(GUI_W\WinMask))
      ClsColor(255, 0, 128)
      Cls
      
      Color(20, 20, 20)
      Rect(0, 0, SizeX, 20, 1)
      Color(255, 255, 255)
      Rect(0, 0, SizeX, SizeY, 0)
      Rect(0, 0, SizeX, 20, 0)
      
      SetFont(FONT_HeadLine)
      Text(5, 2, Name)
         
      Return(GUI_W)
   End Function
   
   Function GUI_WindowUpdate()
      GUI_LastWindow = Null
   
      For GUI_W = Each GUI_Window
         DrawBlock(GUI_W\Buffer, GUI_W\PosX, GUI_W\PosY)
         DrawImage(GUI_W\WinMask, GUI_W\PosX, GUI_W\PosY)
         
         If(GUI_MouseKey1)
            If(RectsOverlap(GUI_MousePosX, GUI_MousePosY, 1, 1, GUI_W\PosX, GUI_W\PosY, GUI_W\SizeX, GUI_W\SizeY))
               GUI_LastWindow = GUI_W
            EndIf
         EndIf
      Next
      
      If(GUI_LastWindow <> Null) Insert GUI_LastWindow After Last GUI_Window
      
      GUI_W = Last GUI_Window
      If(GUI_MouseKey1)
         If(RectsOverlap(GUI_MousePosX, GUI_MousePosY, 1, 1, GUI_W\PosX, GUI_W\PosY, GUI_W\SizeX, 20))
            GUI_WindowDropX = GUI_MousePosX - GUI_W\PosX
            GUI_WindowDropY = GUI_MousePosY - GUI_W\PosY
            GUI_WindowDrop = True         
         EndIf
      EndIf
      
      If(MouseDown(1))
         If(GUI_WindowDrop = True)
            GUI_W\PosX = GUI_MousePosX - GUI_WindowDropX
            GUI_W\PosY = GUI_MousePosY - GUI_WindowDropY
         EndIf
      Else
         GUI_WindowDrop = False
      EndIf
   End Function

;BUTTON
   Type GUI_Button
      Field Window.GUI_Window
      Field Status
      Field Name$
      Field PosX, PosY
      Field SizeX, SizeY
      Field Image
   End Type
   Global GUI_B.GUI_Button

   Function GUI_ButtonCreate.GUI_Button(Window.GUI_Window, Name$, PosX, PosY, SizeX, SizeY)
      GUI_B = New GUI_Button
      GUI_B\Window = Window
      GUI_B\Name = Name
      GUI_B\PosX = PosX
      GUI_B\PosY = PosY
      GUI_B\SizeX = SizeX
      GUI_B\SizeY = SizeY
      
      GUI_B\Image = CreateImage(SizeX, SizeY, 2)
      
      SetBuffer(ImageBuffer(GUI_B\Image, 0))
      Color(0, 0, 0)
      Rect(0, 0, SizeX, SizeY, 1)
      Color(255, 255, 255)
      Rect(0, 0, SizeX, SizeY, 0)
      Text(SizeX / 2 - StringWidth(Name) / 2, SizeY / 2 - StringHeight(Name) / 2, Name)
      
      SetBuffer(ImageBuffer(GUI_B\Image, 1))
      Color(20, 20, 20)
      Rect(0, 0, SizeX, SizeY, 1)
      Color(255, 255, 255)
      Rect(0, 0, SizeX, SizeY, 0)
      Text(SizeX / 2 - StringWidth(Name) / 2, SizeY / 2 - StringHeight(Name) / 2, Name)
      
      Return(GUI_B)
   End Function

   Function GUI_ButtonUpdate()

      For GUI_B = Each GUI_Button
         Frame = 0
         If(GUI_B\Window = Last GUI_Window)
            Frame = 0
         
            GUI_B\Status = False

            If(RectsOverlap(GUI_MousePosX, GUI_MousePosY, 1, 1, GUI_B\PosX + GUI_B\Window\PosX, GUI_B\PosY + GUI_B\Window\PosY, GUI_B\SizeX, GUI_B\SizeY))
               Frame = 1
               If(GUI_MouseKey1) GUI_B\Status = True
            EndIf

         EndIf
      
         SetBuffer(ImageBuffer(GUI_B\Window\Buffer))
         DrawBlock(GUI_B\Image, GUI_B\PosX, GUI_B\PosY, Frame) 
  
      Next

   End Function

;GUI
   Function GUI_Update()

      GUI_WindowUpdate()
      GUI_ButtonUpdate()
      GUI_MouseUpdate()

   End Function

Win3.GUI_Window = GUI_WindowCreate("TestWindow3", 200, 140, 400, 200)
Win2.GUI_Window = GUI_WindowCreate("TestWindow2", 450, 40, 200, 400)
Win1.GUI_Window = GUI_WindowCreate("TestWindow1", 10, 10, 400, 200)

But1.GUI_Button = GUI_ButtonCreate(Win1, "Ende", 10, 30, 100, 20)
But2.GUI_Button = GUI_ButtonCreate(Win2, "Button1", 10, 30, 100, 20)
But3.GUI_Button = GUI_ButtonCreate(Win2, "Button2", 10, 60, 100, 50)

While(KeyHit(1) = False)
   
   If(But1\Status) End
   
   SetBuffer(BackBuffer())
   ClsColor(0, 0, 0)
   Cls()
   
   GUI_Update()
   
   Flip(True)
Wend 