package cm.android.app.flux;

import com.squareup.otto.Subscribe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cm.android.app.flux.action.TestActionCreator;
import cm.android.app.flux.dispatcher.Dispatcher;
import cm.android.app.flux.store.Store;
import cm.android.app.flux.store.TestStore;
import cm.android.app.sample.R;

public class FluxActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button btn;
    private TextView textView;

    private Dispatcher dispatcher;
    private TestActionCreator testActionCreator;
    private TestStore testStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDependencies();
        setupView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.unregister(testStore);
    }

    private void initDependencies() {
        dispatcher = Dispatcher.get();
        testActionCreator = TestActionCreator.get(dispatcher);
        testStore = new TestStore();
        dispatcher.register(testStore);
    }

    private void setupView() {
        editText = (EditText) findViewById(R.id.message_editor);
        textView = (TextView) findViewById(R.id.message_view);
        btn = (Button) findViewById(R.id.message_button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.message_button) {
            if (editText.getText() != null) {
                testActionCreator.send(editText.getText().toString());
                editText.setText(null);
            }
        }
    }

    private void render(TestStore store) {
        textView.setText(store.getModel().getText());
    }

    @Override
    protected void onResume() {
        super.onResume();
        testStore.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        testStore.unregister(this);
    }

    @Subscribe
    public void onStoreChange(Store.StoreChangeEvent event) {
        render(testStore);
    }
}
