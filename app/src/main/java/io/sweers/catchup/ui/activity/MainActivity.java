package io.sweers.catchup.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sweers.catchup.R;
import io.sweers.catchup.ui.PagerController;
import io.sweers.catchup.ui.base.ActionBarProvider;
import io.sweers.catchup.app.CatchUpApplication;
import io.sweers.catchup.util.customtabs.CustomTabActivityHelper;

public class MainActivity extends AppCompatActivity implements ActionBarProvider {

  @Inject CustomTabActivityHelper customTab;

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.controller_container) ViewGroup container;

  private Router router;
  private ActivityComponent component;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    component = createComponent();
    component.inject(this);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    router = Conductor.attachRouter(this, container, savedInstanceState);
    if (!router.hasRootController()) {
      router.setRoot(new PagerController());
    }
  }

  protected ActivityComponent createComponent() {
    return DaggerActivityComponent.builder()
        .applicationComponent(CatchUpApplication.component())
        .activityModule(new ActivityModule(this))
        .build();
  }

  @Override
  protected void onStart() {
    super.onStart();
    customTab.bindCustomTabsService(this);
  }

  @Override
  protected void onStop() {
    customTab.unbindCustomTabsService(this);
    super.onStop();
  }

  @Override
  public void onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed();
    }
  }

  @Override
  protected void onDestroy() {
    customTab.setConnectionCallback(null);
    ButterKnife.unbind(this);
    super.onDestroy();
  }

  @NonNull
  public ActivityComponent getComponent() {
    return component;
  }

}