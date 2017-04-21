package info.limpet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class StoreGroup extends ArrayList<IStoreItem> implements IStoreGroup
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String _name;
  private UUID _uuid;
  private IStoreGroup _parent;
  
  private transient List<StoreChangeListener> _storeListeners;
  private transient List<IChangeListener> _listeners;
  private transient List<PropertyChangeListener> _timeListeners;
  private Date _currentTime;


  public StoreGroup(String name)
  {
    _name = name;
    _uuid = UUID.randomUUID();
  }

  private void checkListeners()
  {
    if(_storeListeners == null)
    {
      _storeListeners =
          new ArrayList<StoreChangeListener>();
    }
    if(_listeners == null)
    {
      _listeners =
          new ArrayList<IChangeListener>();
    }
  }
  

  public void clear()
  {
    // stop listening to the collections individually
    // - defer the clear until the end,
    // so we don't get concurrent modification
    Iterator<IStoreItem> iter = super.iterator();
    while (iter.hasNext())
    {
      IStoreItem iC = iter.next();
      if (iC instanceof Document)
      {
        Document coll = (Document) iC;
        coll.removeChangeListener(this);
      }
    }

    super.clear();
    fireModified();
  }

  public boolean remove(Object item)
  {
    final boolean res = super.remove(item);

    // stop listening to this one
    if (item instanceof Document)
    {
      Document collection = (Document) item;
      collection.removeChangeListener(this);

      // ok, also tell it that it's being deleted
      collection.beingDeleted();
    }

    fireModified();

    return res;
  }
  
  @Override
  public boolean add(IStoreItem item)
  {    
    final boolean res = super.add(item);
    
    item.setParent(this);
    
    item.addChangeListener(this);

    fireModified();

    return res;
  }
  
  @Override
  public void addAll(List<IStoreItem> results)
  {
    // add the items individually, so we can register as a listener
    Iterator<IStoreItem> iter = results.iterator();
    while (iter.hasNext())
    {
      IStoreItem iCollection = iter.next();
      add(iCollection);
    }

    fireModified();
  }

  @Override
  public IStoreGroup getParent()
  {
    return _parent;
  }

  @Override
  public void setParent(IStoreGroup parent)
  {
    _parent = parent;
  }

  @Override
  public String getName()
  {
    return _name;
  }


  @Override
  public void addChangeListener(IChangeListener listener)
  {
    checkListeners();
    
    _listeners.add(listener);
  }

  @Override
  public void removeChangeListener(IChangeListener listener)
  {
    checkListeners();
    
    _listeners.add(listener);
  }
  
  public void addChangeListener(StoreChangeListener listener)
  {
    checkListeners();
    
    _storeListeners.add(listener);
  }

  public void removeChangeListener(StoreChangeListener listener)
  {
    checkListeners();
    
    _storeListeners.remove(listener);
  }


  @Override
  public void fireDataChanged()
  {
    if(_listeners != null)
    {
      for(IChangeListener listener: _listeners)
      {
        listener.dataChanged(this);
      }
    }
  }

  @Override
  public UUID getUUID()
  {
    if (_uuid == null)
    {
      _uuid = UUID.randomUUID();
    }
    return _uuid;
  }


  @Override
  public void dataChanged(IStoreItem subject)
  {
    fireModified();
  }

  @Override
  public void metadataChanged(IStoreItem subject)
  {
    dataChanged(subject);
  }
  

  protected void fireModified()
  {
    checkListeners();

    Iterator<StoreChangeListener> iter = _storeListeners.iterator();
    while (iter.hasNext())
    {
      StoreChangeListener listener =
          iter.next();
      listener.changed();
    }
  }

  @Override
  public void collectionDeleted(IStoreItem subject)
  {
  }
  
  @Override
  public IStoreItem get(UUID uuid)
  {
    IStoreItem res = null;
    Iterator<IStoreItem> iter = iterator();
    while (iter.hasNext())
    {
      IStoreItem item = iter.next();
      if (item instanceof IStoreGroup)
      {
        IStoreGroup group = (IStoreGroup) item;
        Iterator<IStoreItem> iter2 = group.iterator();
        while (iter2.hasNext())
        {
          IStoreItem thisI = (IStoreItem) iter2.next();
          if (uuid.equals(thisI.getUUID()))
          {
            res = thisI;
            break;
          }
        }
      }
      if (uuid.equals(item.getUUID()))
      {
        res = item;
        break;
      }
    }
    return res;
  }

  @Override
  public IStoreItem get(final String name)
  {
    for (final IStoreItem item : this)
    {
      if (item.getName().equals(name))
      {
        // successS
        return item;
      }
      else if(item instanceof IStoreGroup)
      {
        IStoreGroup group = (IStoreGroup) item;
        IStoreItem match = group.get(name);
        if(match != null)
        {
          return match;
        }
      }
    }
    // nope, failed.
    return null;
  }

  @Override
  public Date getTime()
  {
    return _currentTime;
  }

  @Override
  public void setTime(final Date time)
  {
    final Date oldTime = _currentTime;
    _currentTime = time;
    if(_timeListeners != null)
    {
      PropertyChangeEvent evt = new PropertyChangeEvent(this, "TIME", oldTime, time);
      for(PropertyChangeListener thisL: _timeListeners)
      {
        thisL.propertyChange(evt);
      }
    }
  }

  @Override
  public void addTimeChangeListener(PropertyChangeListener listener)
  {
    if(_timeListeners == null)
    {
      _timeListeners = new ArrayList<PropertyChangeListener>();
    }
    _timeListeners.add(listener);
  }

  @Override
  public void removeTimeChangeListener(PropertyChangeListener listener)
  {
    if(_timeListeners != null)
    {
      _timeListeners.remove(listener);
    }
  }

}
