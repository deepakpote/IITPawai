from django.conf.urls import url
from appUser import views

urlpatterns = [
    url(r'^$', views.index, name='index'),
]