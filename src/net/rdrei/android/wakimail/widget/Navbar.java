package net.rdrei.android.wakimail.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.HashMap;

import net.rdrei.android.wakimail.R;

public class Navbar extends RelativeLayout
  implements View.OnClickListener
{
	public abstract interface f
	{
	  public abstract void b(int paramInt);
	}
	
	final class g
	{
	  final View a;
	  final ImageView b;

	  g(View paramView, ImageView paramImageView)
	  {
	    this.a = paramView;
	    this.b = paramImageView;
	  }
	}
	
  private final HashMap a;
  private f b;

  public Navbar(Context paramContext)
  {
    this(paramContext, null);
  }

  public Navbar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.navbarStyle);
  }

  public Navbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    HashMap localHashMap = new HashMap(3);
    this.a = localHashMap;
  }

  private void c(int paramInt1, int paramInt2)
  {
    View localView = findViewById(paramInt1);
    ImageView localImageView = (ImageView)findViewById(paramInt2);
    if (localView == null)
      return;
    g localg = new g(localView, localImageView);
    HashMap localHashMap = this.a;
    Integer localInteger = Integer.valueOf(paramInt1);
    Object localObject = localHashMap.put(localInteger, localg);
    localView.setOnClickListener(this);
  }

  public final void a(int paramInt1, int paramInt2)
  {
    HashMap localHashMap = this.a;
    Integer localInteger = Integer.valueOf(paramInt2);
    g localg = (g)localHashMap.get(localInteger);
    if (localg == null)
      return;
    localg.a.setVisibility(8);
    if (localg.b == null)
      return;
    localg.b.setVisibility(8);
  }

  public final void a(int paramInt, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      int k = paramArrayOfInt[j];
      a(8, k);
      j += 1;
    }
  }

  public final void a(f paramf)
  {
    this.b = paramf;
  }

  public final void b(int paramInt1, int paramInt2)
  {
    HashMap localHashMap = this.a;
    Integer localInteger = Integer.valueOf(2131623974);
    g localg = (g)localHashMap.get(localInteger);
    if (localg == null)
      return;
    ((ImageButton)localg.a).setImageResource(2130837581);
  }

  public void onClick(View paramView)
  {
    if (this.b == null)
      return;
    f localf = this.b;
    int i = paramView.getId();
    localf.b(i);
  }

  protected void onFinishInflate()
  {
    View localView = findViewById(2131623971);
    if (localView != null)
      localView.setOnClickListener(this);
    c(2131624005, 2131624004);
    c(2131623974, 2131624006);
  }
}