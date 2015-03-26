# DnoLib

Android library
with all  can be useful for the applications developement when i don`t have much time to invest in a custom implementation, ok that i will use it always.
the modules are subdivided in:


  - business  
  contains rules that determine how data can be created, displayed, stored, and changed,utilities and useful codesnip.

  - Widgets
  Contains components can be used on ui in java or direct in xml


### Version
0.0.1

## COMPONENTS

##AutoBinding Adapter

Its a simple adapter that allows to map model direct
on view without any lines of code.
That is possible thanks to the annotations added on the model class.

###HOW TO USE
simple instance and set AutoBinder in ListView
```
ArrayList<Example> mList=new ArrayList<Example>();

mListView.setAdapter(new AutoBindAdapter(mList,R.layout.element_example));

```

###MODEL

Bind annotation specify id and type of component

```
public class Example {

        @Bind(to = R.id.name,type = TextView.class)
        public String name;

        @Bind(to = {R.id.img1,R.id.img2},type ={ImageView.class, ImageButton.class})
        public int resourceImage;

        public Example(String name,int resourceId){

            this.name=name;
            this.resourceImage=resourceId;


        }

    }
```
###CUSTOM BINDING
first  extends
AutoBindAdapter
```

public class MyAdapter extends AutoBindAdapter {


/*this method will be execute if
the annotation on field specify
skipAutoBinding=true or if the view is not
supported by internal adapter binding */

@override
protected  void binding(View view,Object value){

    //instructions to set value on view
    if(view.getId()== R.id.name){

       TextView textview=(TextView) view;

       value+=" ciao";

       textview.setText((String)value);

    }
  }

/*
in this method you can do
any transformation before pass value on
binding method
*/

  @override
  protected  Object beforeBinding(View view,Object value){

      //instructions to set value on view
      if(view.getId()== R.id.name){

         TextView textview=(TextView) view;

         value+=" ciao";

         textview.setText((String)value);

      }

    }
}

```

-------------------------------------------------------------
License
----

MIT
