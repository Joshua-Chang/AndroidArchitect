package org.devio.as.hi.hirouter;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.DialogFragmentNavigator;
import androidx.navigation.fragment.FragmentNavigator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.devio.as.hi.hirouter.model.BottomBar;
import org.devio.as.hi.hirouter.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NavUtil {
    private static HashMap<String, Destination> destinations;

    public static String parseFile(Context context, String filename) {
        AssetManager assets = context.getAssets();
        InputStream is = null;
        BufferedReader bfr = null;
        StringBuilder sb=null;
        try {
            is = assets.open(filename);
            bfr = new BufferedReader(new InputStreamReader(is));
            String line = null;
            sb = new StringBuilder();
            while ((line = bfr.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bfr != null) {
                try {
                    bfr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

//    public static void builderNavGraph(FragmentActivity activity, FragmentManager childFm, NavController controller, int containerId) {
    public static void builderNavGraph(FragmentActivity activity, NavController controller, int containerId) {
        String content = parseFile(activity, "destination.json");
        destinations = JSON.parseObject(content, new TypeReference<HashMap<String, Destination>>() {
        });
        NavigatorProvider provider = controller.getNavigatorProvider();
        NavGraphNavigator navGraphNavigator = provider.getNavigator(NavGraphNavigator.class);
        NavGraph navGraph = new NavGraph(navGraphNavigator);

        Iterator<Destination> iterator = destinations.values().iterator();
        while (iterator.hasNext()) {
            Destination destination = iterator.next();
            if (destination.destType.equals("activity")) {
                ActivityNavigator navigator = provider.getNavigator(ActivityNavigator.class);
                ActivityNavigator.Destination node = navigator.createDestination();
                node.setComponentName(new ComponentName(activity.getPackageName(),destination.clazName));
                navGraph.addDestination(node);
            } else if (destination.destType.equals("fragment")) {
                FragmentNavigator navigator = provider.getNavigator(FragmentNavigator.class);
                FragmentNavigator.Destination node = navigator.createDestination();
                node.setId(destination.id);
                node.setClassName(destination.clazName);
                navGraph.addDestination(node);
            } else if (destination.destType.equals("dailog")) {
                DialogFragmentNavigator navigator = provider.getNavigator(DialogFragmentNavigator.class);
                DialogFragmentNavigator.Destination node = navigator.createDestination();
                node.setId(destination.id);
                node.setClassName(destination.clazName);
                navGraph.addDestination(node);
            }
            if (destination.asStarter) {
                navGraph.setStartDestination(destination.id);
            }
        }
        controller.setGraph(navGraph);
    }

    public static void buildBottomBar(BottomNavigationView navView){
        String content = parseFile(navView.getContext(), "main_tabs_config.json");
        BottomBar bottomBar = JSON.parseObject(content, BottomBar.class);
        List<BottomBar.Tab> tabs = bottomBar.tabs;
        Menu menu = navView.getMenu();
        for (BottomBar.Tab tab : tabs) {
            if (!tab.enable) continue;
            Destination destination = destinations.get(tab.pageUrl);//找到pageUrl一样的Destination
            if (destination != null) {
                MenuItem menuItem = menu.add(0, destination.id, tab.index, tab.title);
                menuItem.setIcon(R.drawable.ic_home_black_24dp);
            }
        }
    }

}
