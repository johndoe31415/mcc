int funktion(int x)
{
  return x*x;
}

int zeile(int start,int end,int step,int schwelle)
{
    int dummy;
    int x;
    x:=start;
    while(x <= end) {
      if (funktion(x) >= schwelle){
	 dummy:=writeChar(42);
      }else{
	 dummy:=writeChar(32);
      }
      x:=x+step;
    }
    return writeChar(10);
}

int zeichnen(int x1, int x2, int spalten,
                 int y1, int y2, int zeilen ) {
    int xstep;int ystep;int y;int dummy;
     xstep := (x2 - x1) / spalten;
     ystep := (y2 - y1) / zeilen;

    y:=y2;
    while(y >= y1) {
      dummy:=zeile( x1, x2, xstep, y );
      y:=y-ystep;
    }
    return 0;
}

int main()
{
  return zeichnen(0-2000,2000,64,0,4000000,20);
}
