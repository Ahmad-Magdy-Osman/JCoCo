/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcoco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author xmasterrrr
 */
public class PyFrozenSet extends PyPrimitiveTypeAdapter {
    
    private HashSet<PyObject> data;
    
    public PyFrozenSet() {
        this(new HashSet<PyObject>());
    }
    
    public PyFrozenSet(HashSet<PyObject> data) {
        super("frozenset", PyType.PyTypeId.PyFrozenSetType);
        this.data = data;
        initMethods(funs());
    }
    
    public HashSet<PyObject> hashSet() {
        return this.data;
    }
    
    public int len() {
        return this.data.size();
    }
    
    @Override
    public String str() {
        String str = "frozenset({";
        ArrayList<PyObject> args = new ArrayList<PyObject>();
        int i = 0;
        for (PyObject obj:data) {
            str += ((PyStr) obj.callMethod("__repr__", args)).str();
            if(i < data.size() - 1)
                str += ", ";
            i += 1;
        }
        str += "})";
        return str;
    }
    
    public static HashMap<String, PyCallable> funs() {
        HashMap<String, PyCallable> funs = new HashMap<String, PyCallable>();
        funs.put("__len__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 1)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 1 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                return new PyInt(self.data.size());
            }
        });
        
        funs.put("__or__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                HashSet<PyObject> result = (HashSet<PyObject>) self.data.clone();
                for (int i=0;i<args.size()-1;i++)
                    result.addAll(((PyFrozenSet) args.get(i)).data);
                return new PyFrozenSet(result);
            }
        });
        
        funs.put("__lt__", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(1);
                PyFrozenSet other = (PyFrozenSet) args.get(0);
                return new PyBool(other.data.containsAll(self.data));
            }
        });
        
        funs.put("__ge__", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(1);
                PyFrozenSet other = (PyFrozenSet) args.get(0);
                return new PyBool(self.data.containsAll(other.data));
            }
        });
        
        funs.put("__eq__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                if (self.getType().typeId() != args.get(0).getType().typeId()) {
                    return new PyBool(false);
                }
                PyFrozenSet other = (PyFrozenSet) args.get(0);
                if (self.data.size() != other.data.size())
                    return new PyBool(false);
                return new PyBool(self.data.equals(other.data));
            }
        });
        
        funs.put("__ne__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                PyBool result = (PyBool) self.callMethod("__eq__", selflessArgs(args));
                boolean v = result.getVal();
                if (v)
                    return new PyBool(false);
                return new PyBool(true);
            }
        });
        
        funs.put("__contains__", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self= (PyFrozenSet) args.get(1);
                PyObject element = args.get(0);
                return new PyBool(self.data.contains(element));
            }
        });
        
        funs.put("__notin__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self= (PyFrozenSet) args.get(1);
                PyObject element = args.get(0);
                return new PyBool(!self.data.contains(element));
            }
        });
        
        funs.put("__iter__", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 1)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 1 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                return new PyFrozenSetIterator(self);
            }
        });
        
        funs.put("copy", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args){
                if (args.size()!=1)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 1 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                HashSet<PyObject> result = (HashSet<PyObject>) self.data.clone();
                return new PyFrozenSet(result);
            }
        });
        
        funs.put("union", new PyCallableAdapter() {
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                HashSet<PyObject> result = (HashSet<PyObject>) self.data.clone();
                for (int i=0;i<args.size()-1;i++)
                    result.addAll(((PyFrozenSet) args.get(i)).data);
                return new PyFrozenSet(result);
            }
        });
        
        funs.put("intersection", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                HashSet<PyObject> result = (HashSet<PyObject>) self.data.clone();
                for (int i = 0; i < args.size() - 1; i++)
                    result.retainAll(((PyFrozenSet) args.get(i)).data);
                return new PyFrozenSet(result);
            }
        });
        
        funs.put("issuperset", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(1);
                PyFrozenSet other = (PyFrozenSet) args.get(0);
                return new PyBool(self.data.containsAll(other.data));
            }
        });
        
        funs.put("isdisjoint", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size()!=2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self = (PyFrozenSet) args.get(args.size() - 1);
                HashSet<PyObject> result = (HashSet<PyObject>) self.data.clone();
                for (int i = 0; i < args.size() - 1; i++)
                    result.retainAll(((PyFrozenSet) args.get(i)).data);
                return new PyBool(result.isEmpty());
            }
        });
        
        funs.put("difference", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size()!=2)
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                HashSet<PyObject> result;
                PyFrozenSet self = (PyFrozenSet) args.get(args.size()-1);
                result= (HashSet<PyObject>) self.data.clone();
                for (int i=0;i<args.size()-1;i++)
                    result.removeAll(((PyFrozenSet) args.get(i)).data);
                return new PyFrozenSet(result);
            }
        });
        
        funs.put("symmetric_difference", new PyCallableAdapter(){
            @Override
            public PyObject __call__(ArrayList<PyObject> args) {
                if (args.size() != 2) 
                    throw new PyException(PyException.ExceptionType.PYWRONGARGCOUNTEXCEPTION,
                            "TypeError: expected 2 arguments, got " + args.size());
                PyFrozenSet self= (PyFrozenSet) args.get(args.size()-1);
                PyFrozenSet union= (PyFrozenSet) self.callMethod("union", selflessArgs(args));
                PyFrozenSet intersection=(PyFrozenSet) self.callMethod("intersection", selflessArgs(args));
                union.data.removeAll(intersection.data);
                return new PyFrozenSet(union.data);
            }
        });
        
        return funs;
    }
}
